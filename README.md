# Simulador da Calculadora de Renda Fixa da B3

O objetivo deste projeto é replicar, utilizando a linguagem **JAVA**, o cálculo do fator de DI, acumulado entre duas datas, realizado pela calculadora de renda fixa da B3. Este projeto é destinado exclusivamente para fins de estudo, sem qualquer intenção de prejudicar a plataforma oficial. Pelo contrário, incentivamos o uso da plataforma oficial para realizar cálculos com a maior precisão possível.
Reiteramos que o projeto visa apenas replicar os cálculos realizados pelo site [Calculadora de Renda Fixa](https://calculadorarendafixa.com.br/#/navbar/calculadora).

## Objetivo
O objetivo deste projeto é replicar o cálculo de fator de DI realizado pelo site https://calculadorarendafixa.com.br/#/navbar/calculadora, mais especificamente, o cálculo de fator de DI realizado através dos campos abaixo mostrados na imagem abaixo:

![image](https://github.com/user-attachments/assets/f4682f07-8150-49bf-acb2-3178582948f9)
_Data Inicial - Data Final - Percentual - Valor_

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