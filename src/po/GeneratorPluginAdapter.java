package po;

import java.util.List;

import org.mybatis.generator.api.PluginAdapter;

public class GeneratorPluginAdapter extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		System.out.println("GeneratorPluginAdapter.validate");
		return false;
	}

}
