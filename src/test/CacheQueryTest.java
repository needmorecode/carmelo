package test;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class CacheQueryTest {
	
	public static void main(String asrgs[]){
		Cache myCache1 = CacheManager.getInstance().getCache(
                "server.login.domain.Customer");
        System.out.println("EhBlogTopic cache size:"
                + myCache1.getKeys().size() + ",ÏêÏ¸¼ÇÂ¼ÈçÏÂ£º");
        for (Object str : myCache1.getKeys()) {
            System.out.println(str);
        }
	}

}
