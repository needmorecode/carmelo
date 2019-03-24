package carmelo.examples.server.rpc.action;

import org.springframework.stereotype.Component;

import carmelo.examples.server.rpc.dto.TestArg1Dto;
import carmelo.examples.server.rpc.dto.TestArg2Dto;
import carmelo.examples.server.rpc.dto.TestReturnDto;
import carmelo.servlet.annotation.PassParameter;

@Component
public class RpcTestAction {
	
	public TestReturnDto test(@PassParameter(name = "arg1")TestArg1Dto arg1, @PassParameter(name = "arg2")TestArg2Dto arg2) {
		int sum = arg1.getNum() + arg2.getNum();
		return new TestReturnDto(sum);
	}

}
