import java.lang.reflect.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

class JSONPrinter {

    private static final Class[] SUPPORTED_CLASSES = { BlockHeader.class, Transaction.class, Input.class, Output.class };
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String print(Object obj) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb = addFields(sb, obj, 0);

        return sb.toString();
    }

    private static StringBuilder addFields(StringBuilder sb, Object obj, int depth) throws Exception {

        if(obj == null){
            sb.append("null");
            return sb;
        }

        sb.append("{\n");
        depth++;
        String ident = repeat(" ", depth);

        Field[] fields = obj.getClass().getFields();

        int index = 0;
        for(Field f : fields) {

            Class type = f.getType();

            String value = null;

            sb.append(ident);
            sb.append(f.getName());
            sb.append(" : ");

            if(type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class)){
                value = f.getInt(obj)+"";
            }
            else if(type.isAssignableFrom(Long.class) || type.isAssignableFrom(long.class)){
                value = f.getLong(obj)+"";
            }
            else if(type.isAssignableFrom(LocalDateTime.class)){
                value = ((LocalDateTime)f.get(obj)).format(FORMATTER);
            }
            else if(type.isAssignableFrom(String.class)){
                value = f.get(obj).toString();
            }
            else if(type.isAssignableFrom(List.class)){
                sb.append("[");
                List os = (List)f.get(obj);
                for(Object o : os){
                    addFields(sb, o, depth);
                }
                sb.append("]");
            }

            if(value != null){
                sb.append(value);
            } else if(isSupportedClass(type)) {
                addFields(sb, f.get(obj), depth);
            }

            if(index < fields.length - 1){
                sb.append(",");
            }
            index ++;
            sb.append("\n");
        }

        sb.append(repeat(" ", depth-1));
        sb.append("}");

        return sb;
    }

    private static boolean isSupportedClass(Class type) {
        for (Class c : SUPPORTED_CLASSES) {
            if(type.isAssignableFrom(c)){
                return true;
            }
        }
        return false;
    }

    public static String repeat(String s, int n) {

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < n; i++){
            builder.append("    ");
        }
        return builder.toString();
    }
}
