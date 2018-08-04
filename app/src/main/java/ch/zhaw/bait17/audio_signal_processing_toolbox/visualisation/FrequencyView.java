package ch.zhaw.bait17.audio_signal_processing_toolbox.visualisation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Represents a frequency-based view.
 *
 * @author georgrem, stockan1
 */

public abstract class FrequencyView extends AudioView {

    public FrequencyView(Context context) {
        super(context);
    }

    public FrequencyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FrequencyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Sets the resolution of the FFT. Sometimes called the FFT windows size.
     * The input value is usually a power of 2.
     * For good results the window size should be in the range [2^11, 2^15].
     * The input value should not exceed 2^15.
     *
     * @param fftResolution     power of 2 in the range [2^11, 2^15]
     */
    public abstract void setFFTResolution(int fftResolution);

    /**
     * Sets the name of the window function being used.
     *
     * @param windowName        the name of the window function
     */
    public abstract void setWindowName(String windowName);

    /**
     * Sets the magnitude floor used in the visualisation.
     *
     * @param dBFloor   the magnitude floor
     */
    public abstract void setMagnitudeFloor(int dBFloor);

    /**
     * Sets the power spectral density to be displayed in the {@code FrequencyView}.
     * {@code preFilterMagnitude} represents the power spectral density of the unfiltered samples.
     * {@code postFilterMagnitude} represents the power spectral density of the filtered samples.
     *
     * @param preFilterMagnitude        unfiltered magnitude data
     * @param postFilterMagnitude       filtered magnitude data
     */
    public abstract void setSpectralDensity(@NonNull float[] preFilterMagnitude,
                                            @NonNull float[] postFilterMagnitude);
}
