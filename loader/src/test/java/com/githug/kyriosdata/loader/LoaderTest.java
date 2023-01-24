package com.githug.kyriosdata.loader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoaderTest {

    private String tmpDir;

    @BeforeAll
    public void setUp() throws IOException {
        tmpDir = System.getProperty("java.io.tmpdir");
        copyToTmp("x.jar", tmpDir);
        copyToTmp("y.zip", tmpDir);
        copyToTmp("servico.zip", tmpDir);
        copyToTmp("teste.zip", tmpDir);
    }

    private void copyToTmp(String name, String dir) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(name);
        Path p = Paths.get(dir, name);
        OutputStream os = new FileOutputStream(p.toFile());
        is.transferTo(os);
    }

    @Test
    public void jar() throws Exception{
        Path jar = Paths.get(tmpDir, "x.jar");
        Function<String,String> function = (Function) Loader.get(jar, "Teste");
        assertEquals("OK", function.apply("ok"));
    }

    @Test
    public void zip() throws Exception{
        Path jar = Paths.get(tmpDir, "y.zip");
        Function<String,String> function = (Function) Loader.get(jar, "Teste");
        assertEquals("Y", function.apply("y"));
    }

    @Test
    public void pathArquivoNaoPodeSerNull() {
        assertThrows(NullPointerException.class, () -> Loader.get(null));
    }

    @Test
    public void classeInvalidaOuNaoImplementadaGeraExcecao() {
        assertThrows(LoaderException.class, () -> Loader.get("c:/temp/servico.zip"));
    }

    @Test
    public void forneceApenasArquivo() throws Exception{
        Path jar = Paths.get(tmpDir, "teste.zip");
        String path = jar.toString();
        Function<String,String> function = (Function) Loader.get(path);
        assertEquals("TESTE", function.apply("teste"));
    }
}
