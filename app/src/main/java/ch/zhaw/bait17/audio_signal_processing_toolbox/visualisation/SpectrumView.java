package ch.zhaw.bait17.audio_signal_processing_toolbox.visualisation;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import ch.zhaw.bait17.audio_signal_processing_toolbox.R;
import ch.zhaw.bait17.audio_signal_processing_toolbox.util.ApplicationContext;

/**
 * @author georgrem, stockan1
 */
public class SpectrumView extends FrequencyView {

    private static final int MAX_FREQUENCY = 10000;

    private Context context;
    private GraphView graphView;
    private LineGraphSeries<DataPoint> preFilterSeries;
    private LineGraphSeries<DataPoint> postFilterSeries;
    private int fftResolution;
    private String windowName;

    public SpectrumView(Context context) {
        super(context);
        this.context = context;
        initGraph();
    }

    public View getGraphView() {
        if (graphView == null)
            graphView = (GraphView) View.inflate(ApplicationContext.getAppContext(),
                    R.layout.spectrum_view, null);
        return graphView;
    }

    @Override
    public void setSpectralDensity(@NonNull float[] preFilterMagnitude, @NonNull float[] postFilterMagnitude) {
        if (preFilterMagnitude.length > 0) {
            preFilterSeries = new LineGraphSeries(getDataPoints(preFilterMagnitude));
            initPreFilterSeries();
        }
        if (postFilterMagnitude.length > 0) {
            postFilterSeries = new LineGraphSeries(getDataPoints(postFilterMagnitude));
            initPostFilterSeries();
        }

        graphView.post(new Runnable() {
            @Override
            public void run() {
                graphView.removeAllSeries();
                if (preFilterSeries != null)
                    graphView.addSeries(preFilterSeries);
                if (postFilterSeries != null)
                    graphView.addSeries(postFilterSeries);
            }
        });
    }

    @Override
    public AudioView getInflatedView() {
        return new SpectrumView(context);
    }

    /**
     * Sets the resolution of the FFT. Sometimes called the FFT window size.
     * The input value is usually a power of 2.
     * For good results the window size should be in the range [2^11, 2^15].
     * The input value should not exceed 2^15.
     *
     * @param fftResolution     power of 2 in the range [2^11, 2^15]
     */
    @Override
    public void setFFTResolution(int fftResolution) {
        this.fftResolution = fftResolution;
    }

    /**
     * Sets the name of the window function being used.
     *
     * @param windowName        the name of the window function
     */
    @Override
    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    /**
     * Sets the magnitude floor used in the visualisation.
     *
     * @param dBFloor   the magnitude floor
     */
    @Override
    public void setMagnitudeFloor(int dBFloor) {
        if (graphView != null && dBFloor < 0) {
            graphView.getViewport().setMinY(dBFloor);
        }
    }

    private void initGraph() {
        getGraphView();

        // styling grid/labels
        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setHorizontalLabelsColor(Color.DKGRAY);
        gridLabelRenderer.setVerticalLabelsColor(Color.DKGRAY);
        gridLabelRenderer.setHorizontalAxisTitle("Frequency [Hz]");
        gridLabelRenderer.setVerticalAxisTitle("Audio Spectrum [dB]");
        gridLabelRenderer.setHorizontalAxisTitleColor(Color.BLUE);
        gridLabelRenderer.setVerticalAxisTitleColor(Color.BLUE);
        gridLabelRenderer.setPadding(50);

        // styling viewport
        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMaxY(0);
        viewport.setMinY(ApplicationContext.getPreferredDBFloor());

        // styling legend
        LegendRenderer legendRenderer = graphView.getLegendRenderer();
        legendRenderer.setVisible(true);
        legendRenderer.setTextSize(30);
        legendRenderer.setTextColor(Color.BLACK);
        legendRenderer.setBackgroundColor(Color.WHITE);
        legendRenderer.setAlign(LegendRenderer.LegendAlign.BOTTOM);
        legendRenderer.setMargin(10);
        legendRenderer.setWidth(200);
    }

    private void initPreFilterSeries() {
        // styling series
        preFilterSeries.setTitle("PRE_FX");
        preFilterSeries.setColor(Color.BLACK);
        preFilterSeries.setThickness(2);
    }

    private void initPostFilterSeries() {
        // styling series
        postFilterSeries.setTitle("POST_FX");
        postFilterSeries.setColor(Color.RED);
        postFilterSeries.setThickness(2);
    }

    private DataPoint[] getDataPoints(float[] values) {
        int fs = getSampleRate();
        double deltaFreq = (fs / 2.0d) / values.length;
        int count = (int) (MAX_FREQUENCY / deltaFreq);
        float[] dBMag = new float[count];

        float dBMax = Float.MIN_VALUE;
        for (int i = 0; i < count; i++) {
            dBMag[i] = (float) (10 * Math.log10(values[i]));
            if (dBMag[i] > dBMax) {
                dBMax = dBMag[i];
            }
        }

        DataPoint[] dataPoints = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            dataPoints[i] = new DataPoint(i * 2 * deltaFreq, (10 * Math.log10(values[i])) - dBMax);
        }
        return dataPoints;
    }
}
