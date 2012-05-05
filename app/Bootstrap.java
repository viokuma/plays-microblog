import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;

import models.Content;
import models.User;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class Bootstrap extends Job {
	
	@Override
	public void doJob() throws Exception {
		if (Play.mode.isDev() && Play.id.equals("test")) {
			Fixtures.deleteDatabase();
			Fixtures.loadModels("data.yml");
			
			// 导入更多的测试数据
			User user = User.findByUsername("viokuma");
			for (int i = 0; i < 50; i++) {
				new Content("my add: " + RandomStringUtils.randomNumeric(i), user).save();
			}
		}
	}
	
}
