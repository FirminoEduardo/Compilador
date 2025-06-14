# Compilador CangaCode2025-1

Este projeto implementa um compilador para a linguagem **CangaCode2025-1** usando **Java** e **Maven**. O compilador faz análise léxica e sintática, gera arquivos de tokens e tabela de símbolos.

---

## 📂 Estrutura do Repositório

```text
Compilador2025/
├── pom.xml                   # Configurações Maven
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── lexer/         # Analisador Léxico
│   │   │   ├── parser/        # Analisador Sintático
│   │   │   ├── symboltable/   # Tabela de Símbolos
│   │   │   └── main/          # Classe de entrada (Main)
│   │   └── resources/         # Recursos (se houver)
│   └── test/                  # Testes automatizados (JUnit)
└── README.md                 # Documentação do projeto
```

---

## 🚀 Funcionalidades

- **Análise Léxica**: identifica palavras-chave, operadores, literais e identificadores.
- **Filtragem de Comentários**: suporte a `// comentário` e `/* comentário */`.
- **Truncagem de Tokens**: respeita limites máximos de tamanho (identifiers, strings, números).
- **Análise Sintática**: valida a estrutura do programa, declarações, funções e expressões.
- **Tabela de Símbolos**: armazena variáveis, parâmetros e funções, impedindo duplicações e usos não declarados.
- **Geração de Relatórios**:
  - `<Arquivo>.lex` — lista de tokens reconhecidos.
  - `<Arquivo>.tab` — tabela de símbolos final.

---

## 📥 Instalação

Siga o [Manual de Instalação](#manual-de-instalação) ou use estes passos rápidos:

1. Garanta que **Java JDK 21** e **Maven 3.6+** estejam instalados:
   ```bash
   java -version    # openjdk 21.0.x
   mvn -version
   ```
2. No diretório raiz (onde está `pom.xml`), execute:
   ```bash
   mvn clean package
   ```
3. Verifique o JAR gerado:
   ```bash
   ls target/compilador-1.0-SNAPSHOT.jar
   ```

---

## ▶️ Uso

1. Coloque seu arquivo-fonte `.251` ao lado do JAR ou informe o caminho completo.
2. Execute:
   ```bash
   java -jar target/compilador-1.0-SNAPSHOT.jar MeuPrograma.251
   ```
3. Serão criados:
   - `MeuPrograma.lex` (lista de tokens)
   - `MeuPrograma.tab` (tabela de símbolos)
4. Saída no console:
   ```text
   Programa sintaticamente correto!
   Arquivos gerados: MeuPrograma.lex, MeuPrograma.tab
   ```

---

## 📝 Exemplos

```bash
# Arquivo de exemplo
echo -e "program Ex
declarations
endDeclararions
endProgram" > exemplo.251

# Executando
java -jar target/compilador-1.0-SNAPSHOT.jar exemplo.251
```

---

## 🤝 Contribuição

1. Abra uma *issue* descrevendo sua sugestão.
2. Crie uma *branch*: `git checkout -b nova-feature`.
3. Faça *commit* e então um *pull request*.

---

## 📜 Licença

Projeto acadêmico para disciplina de Compiladores na UCSal.\
Uso sujeito às normas da instituição.

---

## 📖 Manual de Instalação

Veja o **Manual de Instalação** completo em `README.md` ou abaixo:

```markdown
# Manual de Instalação

1. Pré-requisitos:
   - Java JDK 21
   - Maven 3.6+
2. `mvn clean package`
3. `java -jar target/compilador-1.0-SNAPSHOT.jar`
```

*Fim do README*

