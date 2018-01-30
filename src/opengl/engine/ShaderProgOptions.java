/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.engine;

/**
 *
 * @author Andrey
 */
public class ShaderProgOptions {
    protected boolean useGeometryShader;
    protected boolean useTransformationFeedback;
    protected String[] feedbackVaryings;
    

    public ShaderProgOptions() {
    }

    public ShaderProgOptions(boolean useGeometryShader) {
        this.useGeometryShader = useGeometryShader;
    }

    public boolean isUseGeometryShader() {
        return useGeometryShader;
    }

    public void setUseGeometryShader(boolean useGeometryShader) {
        this.useGeometryShader = useGeometryShader;
    }

    public boolean isUseTransformationFeedback() {
        return useTransformationFeedback;
    }

    public void setUseTransformationFeedback(boolean useTransformationFeedback) {
        this.useTransformationFeedback = useTransformationFeedback;
    }

    public String[] getFeedbackVaryings() {
        return feedbackVaryings;
    }

    public void setFeedbackVaryings(String[] feedbackVaryings) {
        this.feedbackVaryings = feedbackVaryings;
    }
}
