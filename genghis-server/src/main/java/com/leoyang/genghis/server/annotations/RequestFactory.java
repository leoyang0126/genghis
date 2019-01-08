package com.leoyang.genghis.server.annotations;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yang.liu on 2018/11/15
 */
public class RequestFactory {
    public static volatile Map<String,String> reqMapping = new ConcurrentHashMap();

    private static final String CLASS_SUFFIX = ".class";

    private static final String CLASS_FILE_PREFIX = File.separator + "classes" + File.separator;

    private static final String PACKAGE_SEPARATOR = ".";

    private static Logger logger = LoggerFactory.getLogger(RequestFactory.class);

    static {
        List<String> classNamelist = RequestFactory.getClazzName("com.leoyang.genghis.server", true);

        // logger.info("RequestFactory init starting");
        for (String className : classNamelist) {
            try {
                Class clazz = ClassUtils.getClass(className);
                Annotation[] annotations = clazz.getAnnotations();
                if ( annotations.length > 0) {
                    // System.out.println("-----"+className);
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Controller) {
                           Method[] methods = clazz.getDeclaredMethods();
                           for (Method method : methods) {
                               Annotation[] methodan = method.getAnnotations();
                               for (Annotation ma : methodan) {
                                   if (ma instanceof RequestMapping) {
                                       RequestMapping requestMapping = (RequestMapping) ma;
                                       reqMapping.put(requestMapping.value(),className + "|" +method.getName());
                                   }
                               }
                           }

                        }
                    }

                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

//        for (Map.Entry<String,String> entry : reqMapping.entrySet()) {
////            System.out.println("reqMapping:{"+entry.getKey()+":"+entry.getValue()+"}");
////        }
    }

    /**
     *
     * 查找包下的所有类的名字
     *
     *
     * @param packageName
     * @param showChildPackageFlag 是否需要显示子包内容
     *
     * @return List集合，内容为类的全名
     *
     */

    public static List<String> getClazzName(String packageName, boolean showChildPackageFlag) {

        List<String> result = new ArrayList<>();

        String suffixPath = packageName.replaceAll("\\.", "/");

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {

            Enumeration<URL> urls = loader.getResources(suffixPath);

            while (urls.hasMoreElements()) {

                URL url = urls.nextElement();

                if (url != null) {

                    String protocol = url.getProtocol();

                    if ("file".equals(protocol)) {

                        String path = url.getPath();

                        //System.out.println(path);

                        result.addAll(getAllClassNameByFile(new File(path), showChildPackageFlag));

                    } else if ("jar".equals(protocol)) {

                        JarFile jarFile = null;

                        try {

                            jarFile = ((JarURLConnection) url.openConnection()).getJarFile();

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                        if (jarFile != null) {

                            result.addAll(getAllClassNameByJar(jarFile, packageName, showChildPackageFlag));

                        }

                    }

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        return result;

    }


    /**
     *
     * 递归获取所有class文件的名字
     *
     *
     * @param file
     * @param flag 是否需要迭代遍历
     *
     * @return List
     *
     */

    private static List<String> getAllClassNameByFile(File file, boolean flag) {

        List<String> result = new ArrayList<>();

        if (!file.exists()) {

            return result;

        }

        if (file.isFile()) {

            String path = file.getPath();

            // 注意：这里替换文件分割符要用replace。因为replaceAll里面的参数是正则表达式,而windows环境中File.separator="\\"的,因此会有问题

            if (path.endsWith(CLASS_SUFFIX)) {

                path = path.replace(CLASS_SUFFIX, "");

                // 从"/classes/"后面开始截取

                String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())

                        .replace(File.separator, PACKAGE_SEPARATOR);

                if (-1 == clazzName.indexOf("$")) {

                    result.add(clazzName);

                }

            }

            return result;


        } else {

            File[] listFiles = file.listFiles();

            if (listFiles != null && listFiles.length > 0) {

                for (File f : listFiles) {

                    if (flag) {

                        result.addAll(getAllClassNameByFile(f, flag));

                    } else {

                        if (f.isFile()) {

                            String path = f.getPath();

                            if (path.endsWith(CLASS_SUFFIX)) {

                                path = path.replace(CLASS_SUFFIX, "");

                                // 从"/classes/"后面开始截取

                                String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())

                                        .replace(File.separator, PACKAGE_SEPARATOR);

                                if (-1 == clazzName.indexOf("$")) {

                                    result.add(clazzName);

                                }

                            }

                        }

                    }

                }

            }

            return result;

        }

    }



    /**
     * 118
     * 递归获取jar所有class文件的名字
     * 119
     *
     * @param jarFile     120
     * @param packageName 包名
     *                    121
     * @param flag        是否需要迭代遍历
     *                    122
     * @return List
     * 123
     */

    private static List<String> getAllClassNameByJar(JarFile jarFile, String packageName, boolean flag) {

        List<String> result = new ArrayList<>();

        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {

            JarEntry jarEntry = entries.nextElement();

            String name = jarEntry.getName();

            // 判断是不是class文件

            if (name.endsWith(CLASS_SUFFIX)) {

                name = name.replace(CLASS_SUFFIX, "").replace("/", ".");

                if (flag) {

                    // 如果要子包的文件,那么就只要开头相同且不是内部类就ok

                    if (name.startsWith(packageName) && -1 == name.indexOf("$")) {

                        result.add(name);

                    }

                } else {

                    // 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类

                    if (packageName.equals(name.substring(0, name.lastIndexOf("."))) && -1 == name.indexOf("$")) {

                        result.add(name);

                    }

                }

            }

        }

        return result;

    }


    public static void main(String[] args) {

        List<String> list = RequestFactory.getClazzName("com.leoyang.genghis.server", true);

        for (String string : list) {

            System.out.println(string);

        }

        for (String className : list) {
            try {
                Class clazz = ClassUtils.getClass(className);
                Annotation[] annotations = clazz.getAnnotations();
                if (annotations.length>0) {
                    System.out.println("-----"+className);
                    // System.out.println(annotation);
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Controller) {
                            System.out.println("----------" + className);
                        }
                    }

                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public static Object invoke(String path, Object... args) {
        String handle = reqMapping.get(path);
        Object resObj = new Object();
        if (StringUtils.isBlank(handle)) {
            return "The corresponding mapping was not found";
        }
        String[] clazzStr = StringUtils.split(handle,"|");
        try {
            Class clazz = ClassUtils.getClass(clazzStr[0]);
            Class[] parmaClass = new Class[args.length];
            for (int index =0; index<args.length ; index++) {
                parmaClass[index] = args[index].getClass();
            }
            Method method = clazz.getDeclaredMethod(clazzStr[1],parmaClass);
            resObj = method.invoke(clazz.newInstance(),args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resObj;
    }

    public static JSONObject strToJsonOjb(Map<?,?> map) {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getKey().toString().contains("@")) {
                //System.out.println("----》" + entry.getKey().toString());
                //System.out.println("----》" + StringUtils.remove(entry.getKey().toString(),"@"));
                String[] strings = (String[])entry.getValue();
                jsonObject.put(StringUtils.remove(entry.getKey().toString(),"@"),strings[0]);
                //System.out.println("--->"+ strings[0]);
            }
        }
        return jsonObject;
    }
}