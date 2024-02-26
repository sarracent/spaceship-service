package w2m.travel.spaceshipsservice.commons.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* w2m.travel.spaceshipsservice.controller.SpaceshipController.getSpaceshipById(..)) && args(id,..)")
    public void logBeforeAccessingSpaceship(Long id) {
        if (id < 0) {
            System.out.println("Attempted to access spaceship with negative ID: " + id);
        }
    }
}
