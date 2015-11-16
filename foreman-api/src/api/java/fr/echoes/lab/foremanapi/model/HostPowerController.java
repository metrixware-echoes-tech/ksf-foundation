package fr.echoes.lab.foremanapi.model;



public class HostPowerController {
//	public static class PowerAction {
//	    @JsonProperty public String power_action;
//	}
//	public static class PowerStatus {
//        @JsonProperty public String power;
//    }
//
//	public enum POWER_ACTION {
//		ON { @Override
//		public String toString() {return "start";}},
//		OFF { @Override
//		public String toString() {return "stop";}},
//		STATUS { @Override
//		public String toString() {return "status";}},
//
//		SHUTDOWN //not support
//	}
//
//	protected ForemanVM vm;
//
//	public HostPowerController(ForemanVM vm) {
//		this.vm = vm;
//	}
//
//	public PowerStatus status() {
//	    return powerAction(POWER_ACTION.STATUS);
//	}
//
//	/**
//	 * Power On
//	 * @return
//	 */
//	public PowerStatus on() {
//		return powerAction(POWER_ACTION.ON);
//	}
//
//	public PowerStatus off() {
//		return powerAction(POWER_ACTION.OFF);
//	}
//
//
//
//
//	/**
//	 * perform a power action
//	 * @param action
//	 * @return
//	 */
//	protected PowerStatus powerAction(POWER_ACTION action) {
//	    final PowerAction actionCl = new PowerAction();
//	    actionCl.power_action = action.toString();
//	    return this.vm.getForeman().api().hostPower(Integer.toString(this.vm.id()), actionCl);
//	}
}
