package uz.forall.notes.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER) // Faqat parametrlarga qo‘llanadi
@Retention(RetentionPolicy.RUNTIME) // Runtime da ishlaydi
@Documented
public @interface CurrentUserId {
}
