<p align="center">
  <img  src="https://user-images.githubusercontent.com/105243897/208307769-c229bb3f-0963-499e-a258-3218ddcfe336.png">
</p>

<h1>História</h1>
  <p>O RabbitMQ é parte da segunda geração de message brokers, e veio para sanar alguns gaps da geração anterior, a geração do MSMQ. O RabbitMQ nasce sob o prisma do AMQP, um standard nascido em 2003, na JPMorgan Chase e mais tarde adotado pela Cisco, Apache foundation (com algumas mplementações), Rabbit Technologies e Microsoft.
O standard AMQP nasce da necessidade de um modelo que pudesse ser agnóstico e interoperável. Isso foi demonstrado com o AMQP 1.0. pelo AMQP Working Group em 30 de outubro de 2011, em uma conferência em New York. Nesse evento Microsoft, Red Hat, VMware, Apache, INETCO e IIT Software demonstraram um software rodando o protocolo em uma demonstração de interoperabilidade. No dia seguinte, em 1 de novembro de 2011, a formação do OASIS Technical Committee foi anunciada.</p>

<h1>Oque é</h1>
  <p>É um broker de código aberto que implementa o modelo AMQP (Advanced Message Queuing Protocol, que seria a troca de mensagens entre aplicações). RabbitMQ é escrito na linguagem de programação Erlang, tornando-o flexível e robusto,seu código fonte é publicado sob a licença pública Mozilla.</p>

<h1>Funcionamento</h1>
  <p>O RabbitMQ pode ser usado sempre que você precisar fazer a comunicação entre aplicações. ele trabalha com comunicação assíncrona, nossa aplicação envia as mensagens para uma Exchange e ele se encarrega de organizar as mensagens nas filas. Em caso mais comum, é usado na aplicação que envia dados(mensagens) que precisam ser processados para ele e ele armazena e vai disponibilizar os dados(mensagens) para a outra aplicação que irá fazer trabalho pesado. Na forma mais simples ele  realiza a comunicação entre aplicações. Como na arquitetura simplificada logo abaixo.</p>
  <p align="center">
  <img  src="https://user-images.githubusercontent.com/105243897/167858626-06b10029-b810-49c3-945c-f95ad796ba99.jpg">
</p>

<h1>Como instalar?</h1>
    <p>A última versão do RabbitMQ é 3.10.0. Sua instalação pode ser realizada seguindo os passos demonstrados pelo site do <a href="https://www.rabbitmq.com/download.html">RabbitMQ</a>.</p>
    
<h1>Conceitos</h1>
  <p><p>Algumas de suas características do RabbitMQ são: Nós distribuídos, Pronto para cluster, Alta disponibilidade com filas espelhadas, Multi-protocolo (AMQP | MQTT | PISTO | SMTP | XMPP), Console Web e API Rest (para monitoramento e administração), Seguro: SSL e LDAP, Várias bibliotecas de clientes: Java, .NET, Ruby, Erlang, Python, Clojure, PHP, JavaScript, etc.</p>
  <p><b>Exchange</b> é quem vai definir para qual fila as mensagens recebidas vão. Tipos de Exchanges:Default, direct, fanout, topic e headers.</p>
	<p>Exchange Default: Esse é o exchange padrão fornecido pelo broker e, não possui nome. Apesar disso, tem uma característica muito interessante: toda fila criada é automaticamente associada a ela com uma rota que é igual ao nome da fila. Em miúdos, se eu declarar uma fila com o nome foo e publicar uma mensagem no default exchange com a rota foo esta mensagem será entregue na fila de mesmo nome, dando a impressão que estou me comunicando diretamente com a fila.</p>
	<p>Exchange Direct: Seu comportamento é de encaminhar mensagens que possuam exatamente a mesma rota das filas associadas a este exchange.</p>
	<p>Exchange Fanout: Este exchange simplesmente ignora a rota. Seu comportamento resume-se em mandar uma cópia das mensagens para todas as filas que estão associadas a ele.</p>
	<p>Exchange Topic: Este exchange encaminha mensagens de acordo com a rota definida na mensagem e o padrão definido na associação da fila ao exchange.</p>
	<p>Exchange Headers: Este tipo ignora a rota e encaminha as mensagens usando seu cabeçalho. Então se você possui uma condição mais complexa que uma string para encaminhar a mensagem você pode fazer uso deste tipo de exchange.</p>
  <p><b>Filas(Queue)</b> é onde as mensagens enviadas vão ser armazenadas para serem consumidas por outras aplicações.</p>
  <p><b>Mensagem(Message)</b> são os dados enviados pela sua aplicação.</p>
  <p><b>Ligação(Bind)</b> é a conexão entre a exchange e a fila.</p>  
</p>

<h1>Ferramentas</h1>
  <p><a href="http://tryrabbitmq.com/">RabbitMQ Simulator</a></p>
  
<h1>Exemplos</h1>
  <p><b>#Branch 01:</b> Uso do RabbitMQ com Spring Boot.</p>

<h1>Referências</h1>
  <p><a href="http://nelsonsar.github.io/2013/11/07/RabbitMQ-exchange-types.html">GitHub Nelson</a></p>
  <p><a href="https://gago.io/blog/rabbitmq-1-desacoplamento/#:~:text=O%20RabbitMQ%20nasce%20sob%20o,pudesse%20ser%20agn%C3%B3stico%20e%20interoper%C3%A1vel">Gago</a></p>
  <p><a href="https://www.youtube.com/watch?v=2YWHtbZJ0QI">Canal Full Cycle</a></p>
  <p><a href="https://www.rabbitmq.com/download.html">Site RabbitMQ</a></p>
