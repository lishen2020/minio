package com.minio.ls.util;

import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * DateTime Utils
 * 如果是JDK8的应用，
 * 可以使用Instant代替Date，
 * LocalDateTime代替Calendar，
 * DateTimeFormatter代替SimpleDateFormat，官方给出的解释：simple beautiful strong immutable thread-safe。
 *
 * @author lishen
 * @version 1.0
 * @date 2020/07/23
 */
public class DateTimeUtils {

    public static final String DATETIME_END_WITH_SECONDS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE = "yyyyMMdd";
    public static final String DATE_WITH_HYPHEN = "yyyy-MM-dd";
    public static final String MONTH = "yyyyMM";
    public static final String MONTH_WITH_HYPHEN = "yyyy-MM";
    public static final String YEAR = "yyyy";
    public static final String MONTH_ONLY = "MM";
    public static final String DAY_ONLY = "dd";

    /**
     * jdk8
     * DateTimeFormatter
     *
     * yyyy-MM-dd HH:mm:ss
     * yyyyMMdd
     * yyyy-MM-dd
     * yyyyMM
     * yyyy-MM
     * yyyy
     * MM
     * dd
     */
    public static final DateTimeFormatter DATETIME_END_WITH_SECONDS_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_END_WITH_SECONDS);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE);
    public static final DateTimeFormatter DATE_WITH_HYPHEN_FORMATTER = DateTimeFormatter.ofPattern(DATE_WITH_HYPHEN);
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern(MONTH);
    public static final DateTimeFormatter MONTH_WITH_HYPHEN_FORMATTER = DateTimeFormatter.ofPattern(MONTH_WITH_HYPHEN);
    public static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern(YEAR);
    public static final DateTimeFormatter MONTH_ONLY_FORMATTER = DateTimeFormatter.ofPattern(MONTH_ONLY);
    public static final DateTimeFormatter DAY_ONKY_FORMATTER = DateTimeFormatter.ofPattern(DAY_ONLY);

    /**
     * jdk7
     * SimpleDateFormat ThreadLocal
     *
     * yyyy-MM-dd HH:mm:ss
     * yyyyMMdd
     * yyyy-MM-dd
     * yyyyMM
     * yyyy-MM
     * yyyy
     * MM
     * dd
     */
    public static ThreadLocal<DateFormat> DateTimeEndWithSecondsThreadLocal =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(DATETIME_END_WITH_SECONDS));
    public static ThreadLocal<DateFormat> dateThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE));
    public static ThreadLocal<DateFormat> dateWithHyphenThreadLocal =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE_WITH_HYPHEN));
    public static ThreadLocal<DateFormat> monthThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat(MONTH));
    public static ThreadLocal<DateFormat> monthWithHyphenThreadLocal =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(MONTH_WITH_HYPHEN));
    public static ThreadLocal<DateFormat> yearThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat(YEAR));
    public static ThreadLocal<DateFormat> monthOnlyThreadLocal =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(MONTH_ONLY));
    public static ThreadLocal<DateFormat> dayOnlyThreadLocal =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(DAY_ONLY));

    /**
     * jdk7
     * SimpleDateFormat format datetime from Date
     *
     * @param threadLocal
     * @param date
     * @return {@link String}
     * @author lishen
     */
    public static String simpleDateFormat(@Nonnull ThreadLocal<DateFormat> threadLocal, @Nonnull Date date) {
        Objects.requireNonNull(threadLocal, "ThreadLocal");
        Objects.requireNonNull(date, "Date");
        return threadLocal.get().format(date);
    }

    /**
     * jdk8
     * SimpleDateFormat format datetime from Instant
     *
     * @param threadLocal
     * @param instant
     * @return {@link String}
     * @author lishen
     */
    public static String simpleDateFormat(@Nonnull ThreadLocal<DateFormat> threadLocal, @Nonnull Instant instant) {
        Objects.requireNonNull(threadLocal, "ThreadLocal");
        Objects.requireNonNull(instant, "Instant");
        return threadLocal.get().format(Date.from(instant));
    }

    /**
     * jdk8
     * DateTimeFormatter format datetime from Date
     *
     * @param dateTimeFormatter
     * @param date
     * @return {@link String}
     * @author lishen
     */
    public static String dateTimeFormatter(@Nonnull DateTimeFormatter dateTimeFormatter, @Nonnull Date date) {
        Objects.requireNonNull(dateTimeFormatter, "DateTimeFormatter");
        Objects.requireNonNull(date, "Date");
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTimeFormatter.format(localDateTime);
    }

    /**
     * jdk8
     * DateTimeFormatter format datetime from Instant
     *
     * @param dateTimeFormatter
     * @param instant
     * @return {@link String}
     * @author lishen
     */
    public static String dateTimeFormatter(@Nonnull DateTimeFormatter dateTimeFormatter, @Nonnull Instant instant) {
        Objects.requireNonNull(dateTimeFormatter, "DateTimeFormatter");
        Objects.requireNonNull(instant, "Instant");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTimeFormatter.format(localDateTime);
    }

    /**
     * jdk8
     * DateTimeFormatter format datetime from LocalDateTime
     *
     * @param dateTimeFormatter
     * @param localDateTime
     * @return {@link String}
     * @author lishen
     */
    public static String dateTimeFormatter(@Nonnull DateTimeFormatter dateTimeFormatter,
                                           @Nonnull LocalDateTime localDateTime) {
        Objects.requireNonNull(dateTimeFormatter, "DateTimeFormatter");
        Objects.requireNonNull(localDateTime, "LocalDateTime");
        return dateTimeFormatter.format(localDateTime);
    }

    public static void main(String[] args) {
    }

}
