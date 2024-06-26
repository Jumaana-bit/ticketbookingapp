// package sofe3980;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// import javafx.application.Application;

// /**
//  * App Entry Point
//  *
//  */
// @SpringBootApplication
// public class App 
// {
//     public static void main( String[] args )
//     {
//         SpringApplication.run(Application.class, args);
//     }
// }

package sofe3980;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * App Entry Point
 */
@SpringBootApplication
public class App extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args); // Corrected to use App.class instead of Application.class
    }
}
