package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	// OrderSimpleApiController - /api/v1/simple-orders
	// 초기화된 Proxy 객체만 노출하고, 초기화되지 않은 프록시 객체는 노출하지 않도록 설정
	@Bean
	Hibernate5JakartaModule hibernate5Module() {
		// 강제 지연 로딩 설정 필요시 아래 설정 적용
		// Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
		// hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
		// return hibernate5JakartaModule;
		return new Hibernate5JakartaModule();
	}
}
