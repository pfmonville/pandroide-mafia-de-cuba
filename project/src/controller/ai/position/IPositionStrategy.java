package controller.ai.position;

import model.Box;
import model.SecretID;

public interface IPositionStrategy {

	public SecretID chooseWhatToTake(Integer position, Box box);
}
