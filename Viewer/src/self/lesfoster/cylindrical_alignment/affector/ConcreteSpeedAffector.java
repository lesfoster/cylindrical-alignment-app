package self.lesfoster.cylindrical_alignment.affector;

public class ConcreteSpeedAffector implements SpeedAffector {
	
	private SpeedAffectorTarget target;
	
	public ConcreteSpeedAffector( SpeedAffectorTarget target ) {
		this.target = target;
	}

	@Override
	public void setSlow() {
		int duration = target.getDuration();
		if (duration < SLOW_SPEED_DURATION) {
			target.setDuration(SLOW_SPEED_DURATION);
		}

	}

	@Override
	public void setFast() {
		int duration = target.getDuration();
		if (duration > FAST_SPEED_DURATION) {
			target.setDuration(FAST_SPEED_DURATION);
		}

	}

	@Override
	public void setImmobile() {
		int duration = target.getDuration();
		if (duration != HALTED_DURATION) {
			target.setDuration(HALTED_DURATION);
		}
	}

	@Override
	public void setDuration(int newDuration) {
		int duration = target.getDuration();
		if (duration != newDuration) {
			target.setDuration(newDuration);
		}
	}

}
