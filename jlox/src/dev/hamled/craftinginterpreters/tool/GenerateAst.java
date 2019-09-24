package dev.hamled.craftinginterpreters.tool;

import java.io.IOException;
import java.io.PrintWriter;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(1);
        }

        String outputDir = args[0];
        defineAst(outputDir, "Expr");
    }

    private static void defineAst(
            String outputDir, String baseName)
            throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package dev.hamled.craftinginterpreters.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        writer.println("}");
        writer.close();
    }
}
