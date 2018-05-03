package com.sstyle.server;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Flag;
import com.sstyle.server.mapper.BlogMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SsServerApplicationTests {

	@Autowired
	private BlogMapper blogMapper;

	@Test
	public void contextLoads() {
		List<Article> articleList = blogMapper.queryArticlesByPageAndFlag(0, 6, 2018001);
		System.out.println(articleList);
		List<Article> flagList = blogMapper.queryPageArticlesByDist(0);
		System.out.println(flagList);
	}

}
