package com.githug.kyriosdata.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>
 * Classe utilitária para carregar e criar instâncias de classes
 * implementadas em um arquivo .jar/.zip conhecido em tempo de execução.
 *
 * <p>Casos de Uso:</p>
 * <ul>
 * <li>Não se deseja ter que recompilar o sistema porque uma variação
 * de uma interface ou classe foi criada.</li>
 *
 * <li>Não se espera que o sistema que faça uso deste utilitário
 * tenha que ser interrompido para contemplar uma nova implementação,
 * disponibilizada em tempo de execução.</li>
 *
 * <li>Não se sabe onde o sistema será instalado (em qual diretório)
 * nem será possível usar o classpath para depósito da variação ou
 * acréscimo fornecido pelo arquivo .jar ou .zip.</p>
 *
 * </ul>
 *
 * <p>Observe que um arquivo .zip ou .jar contendo a classe para a
 * qual uma instância deve ser criada pode ser obtido remotamente,
 * persistido em diretório local e, desta forma, habilitar o uso
 * da presente classe pelo método {@link Loader#get(Path, String)}.</p>
 */
public final class Loader {

    /**
     * Evita criar instância desnecessária.
     */
    private Loader() {
    }

    /**
     * Obtém uma instância de uma classe implementada no arquivo fornecido
     * (arquivo .jar ou .zip).
     *
     * @param path   O arquivo .zip ou .jar contendo a implementação da classe.
     * @param classe A classe cuja instância deve ser criada. Deve possuir um
     *               construtor default.
     * @return A instância da classe.
     * @throws LoaderException Indica falha na tentativa de obter
     *                         instância.
     */
    public static Object get(final Path path,
                             final String classe)
            throws LoaderException {
        Objects.requireNonNull(path, "Path não pode ser null");
        Objects.requireNonNull(classe, "Classe não pode ser null");

        try {
            final URL url = path.toUri().toURL();
            final URLClassLoader child = URLClassLoader.newInstance(new URL[]{url});
            final Class<?> aClass = Class.forName(classe, true, child);
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            final String formato = "Falha ao criar objeto da classe %s do jar %s";
            final String msg = String.format(formato, classe, path);
            throw new LoaderException(msg, e);
        }
    }

    /**
     * Obtém uma instância de uma classe implementada no arquivo fornecido.
     * Na raiz do arquivo .jar ou .zip deve conter o arquivo 'servico.txt'.
     * Este arquivo deve possuir uma única linha, contendo o nome completo
     * da classe cuja instância será retornada.
     *
     * @param path Endereço do arquivo .jar ou .zip.
     * @return Objeto que implementa a classe indicada na única linha
     * do arquivo 'servico.txt' disponibilizado na raiz do arquivo
     * .jar ou .zip.
     *
     * @throws IOException Se o arquivo não puder ser lido.
     * @throws LoaderException Se o arquivo 'servico.txt' não for encontrado ou
     * o nome da classe indicada não puder ser carregada.
     */
    public static Object get(String path) throws IOException, LoaderException {
        Objects.requireNonNull(path, "Path não pode ser null");

        JarFile jarFile = new JarFile(path);
        JarEntry entry = jarFile.getJarEntry("servico.txt");
        if (entry == null) {
            throw new LoaderException("Arquivo 'servico.txt' não encontrado");
        }

        String classname = getClassName(jarFile.getInputStream(entry));

        jarFile.close();

        return get(Paths.get(path), classname);
    }

    /**
     * Recupera o conteúdo como sequência de caracteres da entrada
     * indicada.
     *
     * @param input Entrada da qual será recuperada a String.
     *
     * @return String contendo o conteúdo da entrada.
     *
     * @throws IOException Se não for possível carregar o conteúdo indicado.
     */
    public static String getClassName(InputStream input)
            throws IOException {
        return new String(input.readAllBytes(), "UTF-8");

    }
}
