package org.jaitools.jiffle.runtime;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class JiffleDirectRuntimeImpl extends org.jaitools.jiffle.runtime.AbstractDirectRuntime {

    public JiffleDirectRuntimeImpl() {
        super(new String[] {});
    }

    protected void initOptionVars() {
        _outsideValueSet = true;
_outsideValue = 0;
    }
    protected void initImageScopeVars() {
        _imageScopeVarsInitialized = true;
    }

    public void evaluate(double _x, double _y) {
        if (!isWorldSet()) {
            setDefaultBounds();
        }
        if (!_imageScopeVarsInitialized) {
            initImageScopeVars();
        }
        _stk.clear();

        double v_n = 0.0;
        final int _loiy = (int) (-1);
        final int _hi_loiy = (int) (1);
        for(int v_iy = _loiy; v_iy <= _hi_loiy; v_iy++) {
            final int _loix = (int) (-1);
            final int _hi_loix = (int) (1);
            for(int v_ix = _loix; v_ix <= _hi_loix; v_ix++) {
                v_n += readFromImage("world", _x + v_ix, _y + v_iy, 0);
            }
        }
        v_n -= readFromImage("world", _x, _y, 0);
        writeToImage("nextworld", _x, _y, 0, _FN.OR((_FN.EQ(v_n, 3)), (_FN.AND(readFromImage("world", _x, _y, 0), _FN.EQ(v_n, 2)))));
    }
}
