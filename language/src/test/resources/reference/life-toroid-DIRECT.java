package org.jaitools.jiffle.runtime;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class JiffleDirectRuntimeImpl extends org.jaitools.jiffle.runtime.AbstractDirectRuntime {

    public JiffleDirectRuntimeImpl() {
        super(new String[] {});
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
            double v_yy = _y + v_iy;
            v_yy = (_stk.push(_FN.sign(_FN.LT(v_yy, 0))) == null ? Double.NaN : (_stk.peek() != 0 ? (getHeight() - 1.0) : (v_yy)));
            v_yy = (_stk.push(_FN.sign(_FN.GE(v_yy, getHeight()))) == null ? Double.NaN : (_stk.peek() != 0 ? (0) : (v_yy)));
            final int _loix = (int) (-1);
            final int _hi_loix = (int) (1);
            for(int v_ix = _loix; v_ix <= _hi_loix; v_ix++) {
                double v_xx = _x + v_ix;
                v_xx = (_stk.push(_FN.sign(_FN.LT(v_xx, 0))) == null ? Double.NaN : (_stk.peek() != 0 ? (getWidth() - 1.0) : (v_xx)));
                v_xx = (_stk.push(_FN.sign(_FN.GE(v_xx, getWidth()))) == null ? Double.NaN : (_stk.peek() != 0 ? (0) : (v_xx)));
                v_n += readFromImage("world", v_xx, v_yy, 0);
            }
        }
        v_n -= readFromImage("world", _x, _y, 0);
        writeToImage("nextworld", _x, _y, 0, _FN.OR((_FN.EQ(v_n, 3)), (_FN.AND(readFromImage("world", _x, _y, 0), _FN.EQ(v_n, 2)))));
    }
}
