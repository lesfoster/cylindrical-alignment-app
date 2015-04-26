package self.lesfoster.cylindrical_alignment.affector;

public class ConcreteSpeedAffector implements SpeedAffector {
	private SpeedAffectorTarget target;
	
	public ConcreteSpeedAffector( SpeedAffectorTarget target ) {
		this.target = target;
	}

	@Override
	public void setSlow() {
        //simpleU.getViewer().getView().setSceneAntialiasingEnable(false);
		int rotAlpha = target.getSpeedAffectorAlpha();
		RotationInterpolator rotator = target.getSpeedAffectorRotator();
		double[] bounds = target.getSchedulingBounds();
        //rotAlpha.setIncreasingAlphaDuration(SpeedAffector.SLOW_DURATION);
        rotator.setSchedulingBounds(bounds);

	}

	@Override
	public void setFast() {
        //simpleU.getViewer().getView().setSceneAntialiasingEnable(false);
		int rotAlpha = target.getSpeedAffectorAlpha();
		RotationInterpolator rotator = target.getSpeedAffectorRotator();
		double[] bounds = target.getSchedulingBounds();
        //rotAlpha.setIncreasingAlphaDuration(SpeedAffector.FAST_DURATION);
        rotator.setSchedulingBounds(bounds);

	}

	@Override
	public void setImmobile() {
		RotationInterpolator rotator = target.getSpeedAffectorRotator();
        rotator.setSchedulingBounds(null);

	}

	@Override
	public void setDuration(int newDuration) {
		int rotAlpha = target.getSpeedAffectorAlpha();
		RotationInterpolator rotator = target.getSpeedAffectorRotator();
		double[] bounds = target.getSchedulingBounds();
    	if (newDuration > SpeedAffector.SLOW_DURATION)  // For bug in slider impl.
    		newDuration = SpeedAffector.SLOW_DURATION;

    	//rotAlpha.setIncreasingAlphaDuration(newDuration);
    	rotator.setSchedulingBounds(bounds);

	}

}
