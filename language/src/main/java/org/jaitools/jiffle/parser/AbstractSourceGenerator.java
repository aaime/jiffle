/* 
 *  Copyright (c) 2011, Michael Bedward. All rights reserved. 
 *   
 *  Redistribution and use in source and binary forms, with or without modification, 
 *  are permitted provided that the following conditions are met: 
 *   
 *  - Redistributions of source code must retain the above copyright notice, this  
 *    list of conditions and the following disclaimer. 
 *   
 *  - Redistributions in binary form must reproduce the above copyright notice, this 
 *    list of conditions and the following disclaimer in the documentation and/or 
 *    other materials provided with the distribution.   
 *   
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */   

package org.jaitools.jiffle.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.TreeNodeStream;
import org.antlr.stringtemplate.StringTemplateGroup;

import org.jaitools.CollectionFactory;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.JiffleException;
import org.jaitools.jiffle.JiffleProperties;
import org.jaitools.jiffle.parser.CommentFinder;

/**
 * Base class for tree parsers that generate Jiffle runtime source.
 * <p>
 * The runtime source generator is created from the ANTLR grammar
 * ({@code RuntimeSourceGenerator.g}. This class provides a small number
 * of common methods and fields.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public abstract class AbstractSourceGenerator extends ErrorHandlingTreeParser implements SourceGenerator {
    
    /** The runtime model to generate source for. */
    protected Jiffle.RuntimeModel model;

    /** The package name to use for the runtime class. */
    protected String pkgName;

    /** The imports to be included with the runtime class. */
    protected List<String> imports;

    /** The runtime class name. */
    protected String className;

    /** The name of the base class for the runtime class. */
    protected String baseClassName;
    
    /** A counter used in naming variables inserted into the runtime source. */
    protected int varIndex = 0;
    

    /**
     * Constructor called by ANTLR.
     *
     * @param input AST node stream
     */
    public AbstractSourceGenerator(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }
    
    /**
     * Constructor called by ANTLR.
     * 
     * @param input AST node stream
     * @param state parser state (not used by Jiffle directly)
     */
    protected AbstractSourceGenerator(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);

        this.pkgName = JiffleProperties.get(JiffleProperties.RUNTIME_PACKAGE_KEY);
        
        this.imports = CollectionFactory.list();
        String value = JiffleProperties.get(JiffleProperties.IMPORTS_KEY);
        if (value != null && !(value.trim().length() == 0)) {
            this.imports.addAll( Arrays.asList( 
                    value.split(JiffleProperties.RUNTIME_IMPORTS_DELIM) ) );
        }
    }
    
    /**
     * {@inheritDoc}
     */    
    public void setRuntimeModel(Jiffle.RuntimeModel model) {
        this.model = model;
        switch (model) {
            case DIRECT:
                className = JiffleProperties.get(JiffleProperties.DIRECT_CLASS_KEY);
                break;
                
            case INDIRECT:
                className = JiffleProperties.get(JiffleProperties.INDIRECT_CLASS_KEY);
                break;
                
            default:
                throw new IllegalArgumentException("Internal compiler error");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setBaseClassName(String baseClassName) {
        this.baseClassName = baseClassName;
    }

    /**
     * {@inheritDoc}
     */
    public String getSource(String script) throws JiffleException {
        if (model == null) {
            throw new RuntimeException("Runtime model has not been set");
        }
        if (baseClassName == null || baseClassName.trim().length() == 0) {
            throw new RuntimeException("Base class name has not been set");
        }
        
        String commonTemplateFile = JiffleProperties.get(JiffleProperties.COMMON_SOURCE_TEMPLATES_KEY);
        String modelTemplateFile = null;
        switch (model) {
            case DIRECT:
                modelTemplateFile = JiffleProperties.get(JiffleProperties.DIRECT_SOURCE_TEMPLATES_KEY);
                break;
                
            case INDIRECT:
                modelTemplateFile = JiffleProperties.get(JiffleProperties.INDIRECT_SOURCE_TEMPLATES_KEY);
                break;
        }
        
        try {
            InputStream strm = AbstractSourceGenerator.class.getResourceAsStream(commonTemplateFile);
            InputStreamReader reader = new InputStreamReader(strm);
            StringTemplateGroup commonSTG = new StringTemplateGroup(reader);
            reader.close();

            strm = AbstractSourceGenerator.class.getResourceAsStream(modelTemplateFile);
            reader = new InputStreamReader(strm);
            StringTemplateGroup modelSTG = new StringTemplateGroup(reader);
            setTemplateLib(modelSTG);
            reader.close();
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        try {
            setErrorReporter(new DeferredErrorReporter());
            return generate(script).getTemplate().toString();

        } catch (RecognitionException ex) {
            if (errorReporter != null && errorReporter.getNumErrors() > 0) {
                throw new JiffleException(errorReporter.getErrors());
            } else {
                throw new JiffleException(
                        "Error creating runtime source. No details available.");
            }
        }
    }
    
    /**
     * Starts generating source code based on the abstract syntax tree 
     * produced by the Jiffle compiler.
     * 
     * @param script the Jiffle script to include in the class javadocs;
     *        may be {@code null} or empty
     * 
     * @return an ANTLR rule return object from which the results can be
     *         retrieved
     * 
     * @throws RecognitionException on errors processing the AST
     */
    protected abstract RuleReturnScope generate(String script) throws RecognitionException;

    /**
     * Used internally to set the string templates for source generation.
     * Declared public to accord with the underlying ANTLR tree parser.
     * 
     * @param templateLib source generation templates
     */
    public abstract void setTemplateLib(StringTemplateGroup templateLib);
    
    /**
     * Looks up the runtime source for a Jiffle function.
     *
     * @param name function name
     * @param argTypes argument type names; null or empty for no-arg functions
     *
     * @return runtime source
     */
    protected String getRuntimeExpr(String name, List<String> argTypes) {
        try {
            return FunctionLookup.getRuntimeExpr(name, argTypes);
        } catch (UndefinedFunctionException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    
    /**
     * Looks up the runtime source for a Jiffle function.
     *
     * @param name function name
     * @param argTypes argument type names; null or empty for no-arg functions
     *
     * @return runtime source
     */
    protected String getRuntimeExpr(String name, String ...argTypes) {
        return getRuntimeExpr(name, Arrays.asList(argTypes));
    }
    
    
    /**
     * Gets the runtime source for a script option name:value pair.
     * 
     * @param name option name
     * @param value option value
     * @return the runtime source
     */
    protected String getOptionExpr(String name, String value) {
        try {
            return OptionLookup.getActiveRuntimExpr(name, value);
        } catch (UndefinedOptionException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    
    /**
     * Adds the given imports to those that will be included in the
     * runtime source.
     * 
     * @param importNames fully qualified class names
     */
    protected void addImport(String ...importNames) {
        for (String name : importNames) {
            boolean found = false;
            for (String imp : imports) {
                if (imp.equals(name)) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                imports.add(name);
            }
        }
    }

    /**
     * Prepares the Jiffle source for inclusion in the run-time class javadocs.
     * Comments are stripped from the script and it is split into lines.
     * 
     * @param script the Jiffle source
     * 
     * @return prepared script as a list of lines
     */
    protected List<String> prepareScriptForComments(String script) {
        ANTLRStringStream stream = new ANTLRStringStream(script);
        
        CommentFinder finder = new CommentFinder(stream);
        Token tok = finder.nextToken();
        while (tok.getType() != Token.EOF) {
            tok = finder.nextToken();
        }
        
        List<Integer> indices = finder.getStartEndIndices();
        StringBuilder sb = new StringBuilder();
        if (indices.isEmpty()) {
            sb.append(script);
        } else {
            int pos = 0;
            for (int i = 0; i < indices.size() && pos < script.length(); i += 2) {
                int start = indices.get(i);
                int end = indices.get(i+1);
                sb.append(script.substring(pos, start));
                pos = end;
            }
            if (pos < script.length()) {
                sb.append(script.substring(pos, script.length()));
            }
        }
        
        String[] lines = sb.toString().split("[\n\r]+");
        return Arrays.asList(lines);
    }
}

