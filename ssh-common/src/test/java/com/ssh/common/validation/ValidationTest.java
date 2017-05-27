package com.ssh.common.validation;

import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * https://docs.jboss.org/hibernate/validator/5.1/reference/en-US/html/chapter-method-constraints.html
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-validation.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationTest.class);

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void test1ValidateObject() {
        Car car = new Car(null, "DD-AB-123");
        Set<ConstraintViolation<Car>> violations = validator.validate(car, Groups.Add.class);
        Assert.assertEquals(1, violations.size());
        for (ConstraintViolation<Car> violation : violations) {
            LOGGER.debug("{}", violation.getMessage());
        }
    }

    @Test
    public void test2ValidateParameters() throws NoSuchMethodException {
        Car object = new Car("BMW", "DD-AB-123");
        Method method = Car.class.getMethod("drive", int.class);
        Object[] parameterValues = {80};
        Set<ConstraintViolation<Car>> violations = validator.forExecutables().validateParameters(
                object,
                method,
                parameterValues,
                Groups.Add.class
        );
        Assert.assertEquals(1, violations.size());
        for (ConstraintViolation<Car> violation : violations) {
            LOGGER.debug("{}", violation.getMessage());
        }
    }

    @Test
    public void test3ValidateReturnValue() throws NoSuchMethodException {
        Car object = new Car("BMW", "DD-AB-123");
        Method method = Car.class.getMethod("getManufacturer");
        Object returnValue = "";
        Set<ConstraintViolation<Car>> violations = validator.forExecutables().validateReturnValue(object, method, returnValue);
        Assert.assertEquals(1, violations.size());
        for (ConstraintViolation<Car> violation : violations) {
            LOGGER.debug("{}", violation.getMessage());
        }
    }

    public class Car {

        @NotNull(groups = {Groups.Add.class}, message = "厂商不能为空")
        private String manufacturer;

        @NotNull
        @Size(min = 2, max = 14)
        private String licensePlate;

        public Car(String manufacturer, String licencePlate) {
            this.manufacturer = manufacturer;
            this.licensePlate = licencePlate;
        }

        @NotEmpty
        public String getManufacturer() {
            return manufacturer;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public void drive(@Max(value = 75, groups = {Groups.Add.class}, message = "时速不能大于75") int speedInMph) {
        }

    }

}
