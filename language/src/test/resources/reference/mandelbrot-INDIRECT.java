package org.jaitools.jiffle.runtime;

public class JiffleIndirectRuntimeImpl extends org.jaitools.jiffle.runtime.AbstractIndirectRuntime {
    boolean _imageScopeVarsInitialized = false;
    double v_MaxIter;
    double v_MinRe;
    double v_MaxRe;
    double v_MinIm;
    double v_MaxIm;
    double v_Re_scale;
    double v_Im_scale;

    protected void initImageScopeVars() {
        v_MaxIter = 30;
        v_MinRe = - 2.0;
        v_MaxRe = 1.0;
        v_MinIm = - 1.2;
        v_MaxIm = v_MinIm + (v_MaxRe - v_MinRe) * getHeight() / getWidth();
        v_Re_scale = (v_MaxRe - v_MinRe) / (getWidth() - 1);
        v_Im_scale = (v_MaxIm - v_MinIm) / (getHeight() - 1);
        _imageScopeVarsInitialized = true;
    }

    public double evaluate(double _x, double _y) {
        if (!_imageScopeVarsInitialized) {
            initImageScopeVars();
        }
        _stk.clear();

        double v_c_im = v_MaxIm - _y * v_Im_scale;
        double v_c_re = v_MinRe + _x * v_Re_scale;
        double v_Z_re = v_c_re;
        double v_Z_im = v_c_im;
        double v_outside = 0;
        double v_n = 0;
        while (!_FN.isTrue(_FN.GE(v_n, v_MaxIter))) {
            double v_Z_re2 = v_Z_re * v_Z_re;
            double v_Z_im2 = v_Z_im * v_Z_im;
            v_outside = _FN.GT(v_Z_re2 + v_Z_im2, 4);
            if (_FN.isTrue(v_outside)) break;
            v_Z_im = 2 * v_Z_re * v_Z_im + v_c_im;
            v_Z_re = v_Z_re2 - v_Z_im2 + v_c_re;
            v_n++;
        }
        return v_outside;
    }
}
