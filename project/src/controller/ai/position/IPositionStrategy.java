package controller.ai.position;

import controller.ai.Strategy;
import model.Box;
import model.SecretID;

public interface IPositionStrategy extends Strategy{

	public SecretID chooseWhatToTake(Integer position, Box box);
}
