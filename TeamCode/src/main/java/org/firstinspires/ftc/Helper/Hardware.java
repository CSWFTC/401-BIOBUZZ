package org.firstinspires.ftc.Helper;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Reverse {
}

// use this annotation for things that you don't want to be initialized
// because they're not plugged in
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface DoNotInitialize {
}

// singleton for easy access to hardware peripherals
public class Hardware {
    // you can add more peripherals here
    // just declare a new field
    // the field name should match the name in the robot config
    // that's it!
    @Reverse
    public static DcMotor frontLeft;

    public static DcMotor frontRight;

    public static DcMotor rearLeft;
    public static DcMotor rearRight;

    @DoNotInitialize
    public static WebcamName camera;

    @Reverse
    public static DcMotor intake;
    @Reverse
    public static DcMotor outtake;

    @Reverse
    public static DcMotor spindex;


    // initialization code
    public static void init(HardwareMap map) {
        Field[] fields = Hardware.class.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.getAnnotation(DoNotInitialize.class) == null) {
                field.setAccessible(true);
                try {
                    field.set(null, map.get(field.getType(), field.getName()));
                    if (field.getType() == DcMotor.class) {
                        Reverse r = field.getAnnotation(Reverse.class);
                        if (r != null) {
                            DcMotor m = (DcMotor) field.get(null);
                            assert m != null;
                            m.setDirection(DcMotorSimple.Direction.REVERSE);
                        }
                    }
                } catch (IllegalAccessException ignored) {

                }
            }
        }
    }
}