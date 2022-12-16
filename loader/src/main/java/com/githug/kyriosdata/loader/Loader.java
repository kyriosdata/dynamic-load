package com.githug.kyriosdata.loader;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Objects;

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
     *                           instância.
     */
    public static Object get(final Path path,
                             final String classe)
            throws LoaderException {
        Objects.requireNonNull(path, "Path não pode ser null");
        Objects.requireNonNull(classe, "Classe não pode ser null");

        try {
            final URL url = path.toUri().toURL();
            final URLClassLoader child = URLClassLoader.newInstance(new URL[] { url });
            final Class<?> aClass = Class.forName(classe, true, child);
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            final String formato = "Falha ao criar objeto da classe %s do jar %s";
            final String msg = String.format(formato, classe, path);
            throw new LoaderException(msg, e);
        }
    }
}
