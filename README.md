# dynamic-load

Permite a criação de instância de classe disponibilizada em arquivo .jar ou .zip.
O arquivo .jar ou .zip, assim como o nome da classe nele contido, não precisam ser previamente conhecidos.
Estas duas informações são indicadas por meio de duas sequências de caracteres, uma indicando a
localidade do arquivo e o nome completo da classe.

# Caso de Uso

A carga dinâmica oferece possibilidade de correção de erro, melhoria de desempenho ou similar em uma biblioteca que pode ser substituída em tempo de execução, por exemplo.

```java
// Retorna objeto da classe indicada contida
// no arquivo .jar.
Loader.get("arquivo.jar", "nome.completo.Classe");
```

```java
// Retorna objeto cuja classe é indicada
// na primeira e única linha do arquivo
// 'servico.txt' contido na raiz do arquivo
// .zip ou .jar fornecido.
Loader.get("arquivo.zip");
```

# Segurança

O objeto retornado oferece risco, até mesmo durante a
construção da instância. Noutras palavras:

> Use esta estratégia apenas se confiar na implementação fornecida
> no arquivo .zip ou .jar.

# Orientações gerais

No diretório **loader** está o projeto que implementa a funcionalidade de carga dinâmica de arquivo JAR/ZIP.
