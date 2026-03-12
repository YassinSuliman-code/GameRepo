package game.engine.cells;
import game.engine.interfaces.CanisterModifier;

public class ContaminationSock extends TransportCell implements CanisterModifier{
	//supposed to be able to modify energy
	
	public ContaminationSock(String name, int effect){
		//effect always negative
		super(name, effect);
	}
}
