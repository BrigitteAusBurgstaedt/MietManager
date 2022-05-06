package jrp.mietmanager.berechnung;

import javafx.scene.chart.Axis;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class DatumsAchse extends Axis<LocalDate> {

    private LocalDate minDate, maxDate;

    public DatumsAchse() {
    }

    public DatumsAchse(String axisLabel) {
        this();
        setLabel(axisLabel);
    }

    @Override
    protected Object autoRange(double v) {
        return new Object[]{minDate, maxDate};
    }

    @Override
    protected void setRange(Object o, boolean b) {

    }

    @Override
    protected Object getRange() {
        return null;
    }

    @Override
    public double getZeroPosition() {
        return 0;
    }

    @Override
    public double getDisplayPosition(LocalDate localDate) {
        return 0;
    }

    @Override
    public LocalDate getValueForDisplay(double v) {
        return null;
    }

    @Override
    public boolean isValueOnAxis(LocalDate localDate) {
        return false;
    }

    @Override
    public double toNumericValue(LocalDate localDate) {
        return 0;
    }

    @Override
    public LocalDate toRealValue(double v) {
        return null;
    }

    @Override
    protected List<LocalDate> calculateTickValues(double v, Object o) {
        return null;
    }

    @Override
    protected String getTickMarkLabel(LocalDate localDate) {
        return null;
    }
}
