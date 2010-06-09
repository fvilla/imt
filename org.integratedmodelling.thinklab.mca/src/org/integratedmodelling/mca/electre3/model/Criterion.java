package org.integratedmodelling.mca.electre3.model;

import java.io.Serializable;

/**
 *
 * @author Edwin Boaz Soenaryo
 */
public class Criterion implements Serializable {

	private static final long serialVersionUID = 4052105659500943312L;
	public Criterion(String code) {
		
        setCode(code);
        setDescription("");
        setWeight(0.0);
        setAscendingPref(false);
        setVetoEnabled(true);
        setIndifferenceThreshold(0.0, 0.0);
        setPreferenceThreshold(0.0, 0.0);
        setVetoThreshold(0.0, 0.0);
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isAscendingPref() {
        return isAscendingPref;
    }

    public void setAscendingPref(boolean isAscendingPref) {
        this.isAscendingPref = isAscendingPref;
    }

    public boolean isVetoEnabled() {
        return isVetoEnabled;
    }

    public void setVetoEnabled(boolean isVetoEnabled) {
        this.isVetoEnabled = isVetoEnabled;
    }

    public double getIndifferenceThreshold(double performanceValue) {
        return getThresholdValue(indifference, performanceValue);
    }
    
    public double getIndifferenceAlpha() {
        return indifference.getAlpha();
    }
    
    public double getIndifferenceBeta() {
        return indifference.getBeta();
    }
    
    public void setIndifferenceThreshold(double alpha, double beta) {
        this.indifference = new Threshold(alpha, beta);
    }
    
    public double getPreferenceThreshold(double performanceValue) {
        return getThresholdValue(preference, performanceValue);
    }
    
    public double getPreferenceAlpha() {
        return preference.getAlpha();
    }
    
    public double getPreferenceBeta() {
        return preference.getBeta();
    }
    
    public void setPreferenceThreshold(double alpha, double beta) {
        this.preference = new Threshold(alpha, beta);
    }
    
    public double getVetoThreshold(double performanceValue) {
        if (isVetoEnabled())
            return getThresholdValue(veto, performanceValue);
        else
            return 0;
    }
    
    public double getVetoAlpha() {
        return veto.getAlpha();
    }
    
    public double getVetoBeta() {
        return veto.getBeta();
    }
        
    public void setVetoThreshold(double alpha, double beta) {
        this.veto = new Threshold(alpha, beta);
    }
    
    @Override
    public String toString() {
        return code;
    }
    
    private double getThresholdValue(Threshold thres, double performanceValue) {
        double alpha = thres.getAlpha();
        double beta = thres.getBeta();
        return alpha * performanceValue + beta;
    }
    
    private String code;
    private String description;
    private double weight;
    private boolean isAscendingPref;
    private boolean isVetoEnabled;
    private Threshold indifference;
    private Threshold preference;
    private Threshold veto;
    
}