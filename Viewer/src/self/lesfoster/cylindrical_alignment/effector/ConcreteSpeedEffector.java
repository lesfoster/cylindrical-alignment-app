package self.lesfoster.cylindrical_alignment.effector;

public class ConcreteSpeedEffector implements SpeedEffector {
	
	private SpeedEffectorTarget target;
	
	public ConcreteSpeedEffector( SpeedEffectorTarget target ) {
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
