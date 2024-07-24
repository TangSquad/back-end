//package backend.tangsquad;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.Collections;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfiguration {
//
//    @Bean
//    public Docket apiV1() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("groupName1")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("backend.tangsquad"))
//                .paths(PathSelectors.ant("/v1/api/**"))
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfo(
//                "Title",
//                "Description",
//                "version 1.0",
//                "https://naver.com",
//                new Contact("Contact Me", "https://daum.net", "colt@colt.com"),
//                "Edit Licenses",
//                "https://naver.com",
//                Collections.emptyList()
//        );
//    }
//}
