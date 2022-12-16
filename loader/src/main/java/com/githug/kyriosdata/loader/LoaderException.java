package com.githug.kyriosdata.loader;

/**
 * Representa situação excepcional ao carregar
 * e tentar obter uma instância de uma dada classe.
 */
public class LoaderException extends Exception {

    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cria uma exceção com a descrição fornecida.
     * @param msg Descrição da exceção.
     */
    public LoaderException(final String msg) {
        super(montaMsg(msg));
    }

    /**
     * Detalha exceção com a mensagem fornecida.
     *
     * @param msg Mensagem que detalha a exceção.
     * @param exp Exceção original.
     */
    public LoaderException(final String msg, final Throwable exp) {
        super(montaMsg(msg), exp);
    }

    private static String montaMsg(final String msg) {
        return "LoaderException: " + msg;
    }
}
