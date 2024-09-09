# Simulador da Calculadora de Renda Fixa da B3

O objetivo deste projeto é replicar, utilizando a linguagem **JAVA**, o cálculo do fator de DI, acumulado entre duas datas, realizado pela calculadora de renda fixa da B3. Este projeto é destinado exclusivamente para fins de estudo, sem qualquer intenção de prejudicar a plataforma oficial. Pelo contrário, incentivamos o uso da plataforma oficial para realizar cálculos com a maior precisão possível.
Reiteramos que o projeto visa apenas replicar os cálculos realizados pelo site [Calculadora de Renda Fixa](https://calculadorarendafixa.com.br/#/navbar/calculadora).

## Objetivo
O objetivo deste projeto é replicar o cálculo de fator de DI realizado pelo site https://calculadorarendafixa.com.br/#/navbar/calculadora, mais especificamente, o cálculo de fator de DI realizado através dos campos abaixo mostrados na imagem abaixo:

![image](https://github.com/user-attachments/assets/f4682f07-8150-49bf-acb2-3178582948f9)
<br>_Data Inicial - Data Final - Percentual - Valor_

## B3
Abaixo seguirão alguns links da plataforma B3 que foram utilizados para realizar cálculos e pesquisas:

|                        | Link                                                                                                                                                          |
| ---------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Calculadora Renda Fixa | https://calculadorarendafixa.com.br/#/navbar/calculadora                                                                                                      |
| Caderno de Fórmulas    | https://www.b3.com.br/pt_br/produtos-e-servicos/negociacao/caderno-de-formulas/                                                                               |
| Índice B3              | https://www.b3.com.br/pt_br/market-data-e-indices/indices/indices-de-segmentos-e-setoriais/serie-historica-do-di.htm                                          |
| Série Histórica        | http://estatisticas.cetip.com.br/astec/series_v05/paginas/lum_web_v05_template_informacoes_di.asp?str_Modulo=completo&int_Idioma=1&int_Titulo=6&int_NivelBD=2 |

## Tecnologias
Para este projeto, será utilizado o Java com Spring Boot e Swagger para realizar a requisição de cálculo.

| Tecnologia  | Versão | Documentação                                                       |
| ----------- | ------ | ------------------------------------------------------------------ |
| JAVA        | JDK 17 | https://docs.oracle.com/en/java/javase/17/                         |
| Spring Boot | 3.3.3  | https://github.com/spring-projects/spring-boot/releases/tag/v3.3.3 |
| Swagger     | 2.5.0  | https://springdoc.org/#Introduction                                |

### Spring Initializr
O projeto Spring Boot foi criado à partir da plataforma [Spring Initializr](https://start.spring.io/), a versão que será utilizada é a versão recomendada no momento, dia 08/09/2024, talvez tenham mudanças futuras à esta data (exemplo de mudanças: a versão recomendada ser outra).

### Contract-First em REST com Swagger
Para o projeto foi utilizado o Swagger, o modelo adotado foi o Contract-First onde, há documento .yml dentro da pasta _resources_ contendo a chamada do endpoint no Swagger. Para acessar o swagger basta acessar o link: http://localhost:8080/swagger-ui/index.html.
A escrita e revisão do Swagger foi definida através do link do Swagger Editor: https://editor.swagger.io/.

## Desenvolvimento

### Primeiro Passo:
Como primeiro passo para o desenvolvimento, deve-se verificar e garantir que a data final seja um dia útil.
Na B3, mesmo quando calculamos e a data final caia em um final de semana, o valor calculado é sempre do próximo dia útil, o motivo disso é que um resgate de algum saldo, por exemplo, só pode cair em dias úteis e por isso ele continua rendendo até o próximo dia útil onde deverá ser interrompido.

Para realizar a verificação, primeiro fizemos uma solicitação dos feriados nacionais do ano em que a data final é mencionada, verificamos se a data final corresponde com algum feriado e depois verificamos se a data final cai em algum final de semana, caso caia em um destes casos, modificamos a data final para um próximo dia que seja dia útil.

`GET: https://brasilapi.com.br/api/feriados/v1/:ano`

### Segundo Passo:
O segundo passo foi um pouco mais complicado, nele precisávamos descobrir ou o valor da Taxa SELIC de cada um dos dias (do dia correspondente à data inicio até o dia corresponde da data final), ou então conseguir o Fator Diário destes mesmos dias.

Caso obtivéssemos a Taxa SELIC, teríamos que converter para o Fator Diário.

Segue abaixo a fórmula e como desenvolver:

```
    // (1 + txSelic)^(1 / 252)
    BigDecimal txSelicEmDecimal = txSelic.divide(BigDecimal.valueOf(100), MathContext.DECIMAL64);
    BigDecimal somarTxSelicDecimalComUm = BigDecimal.ONE.add(txSelicEmDecimal);
    BigDecimal divisaoPorDiasUteis = BigDecimal.ONE.divide(new BigDecimal("252"), 8, RoundingMode.FLOOR);

    return bigDecimalPow(somarTxSelicDecimalComUm, divisaoPorDiasUteis).setScale(8, RoundingMode.HALF_EVEN);
```

O motivo de eu ter inserido a fórmula e como desenvolver com os arredondamentos acima é simplesmente porque eu posso utilizar uma chamada do Banco Central do Brasil para receber o Fator Diário já quase pronto.

`GET: https://api.bcb.gov.br/dados/serie/bcdata.sgs.11/dados?formato=json&dataInicial=01/01/2024&dataFinal=09/09/2024`

O código 11 em `...sgs.11/dad...` remete ao código de série da taxa SELIC.

É possível saber mais em: https://dadosabertos.bcb.gov.br/dataset/11-taxa-de-juros---selic/resource/b73edc07-bbac-430c-a2cb-b1639e605fa8.

A mudança que teremos que fazer no response da requisição mostrada acima é simplesmente, dividir por 100 o valor do Fator Diário e depois somar com 1 (um).

Depois que descobrir o Fator Diário, deve-se então descobrir o fator da consulta. O fator da consulta é um acúmulo de valores, o primeiro dia nós definimos como 1, no segundo dia que realmente começam os cálculos, o fator do segundo dia é o produto da multiplicação do fator do primeiro dia x (vezes) o fator ajustado com o percentual (`(fator diário - 1) * percentual + 1`) e assim sucessivamente até o último dia, o fator calculado (que é o que queremos) é o resultado do fator do último dia.

### Terceiro Passo:
O terceiro passo foi bem tranquilo e, consistia em descobrir o valor calculado.
O valor calculado é o produto da multiplicação do valor base com o fator já calculado (o fator do último dia descrito no sub-item anterior).

### Quarto Passo:
O quarto e último passo do desenvolvimento é realizar o cálculo da taxa, o cálculo é simplesmente:
`((valor final - valor inicial) / valor inicial) * 100`

<br><hr><br>

Espero que este material ajude em seus estudos e desejo sucesso à todos.

Att. Victor

Para quem se interessar, segue meu Linkedin: https://www.linkedin.com/in/victor-elias-ross-jr/