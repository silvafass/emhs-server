# emhs-server
Emhs Server ou Expose Method Http Service Server, é um projeto que oferece um servidor para Emhs.

---

##### Exemplo Simples

```Java
@Service
public class EmhsExemplo {

	public String hello() {
		return "Hello World";
	}

	public String hello(String nome, Integer idade) {
		return "Ola "+nome+" sua idade é "+idade;
	}

	public static void main(String[] args) throws IOException {
		Emhs.server(8000).start();
	}
}
```
Fonte: [EmhsExemplo.java](https://github.com/silvafass/emhs-exemplo/blob/master/src/br/com/emhs/exemplo/EmhsExemplo.java)

Epenas com esse trexo de codigo ja é possivel ter 2 serviços disponivel:

Caso esteja executando esse exemplo em uma maquina local, pode verificar os serviços no seguinte link pelo navegador:

  * http://localhost:8000/emhsexemplo/hello
  * http://localhost:8000/emhsexemplo/hello?nome=francisco&idade=28

## Outros exemplos
##### Personalizar url de serviço:

```Java
@Service("nomePersonalizado")
public class EmhsExemplo {
```
A url resultante seria:
  * http://localhost:8000/nomepersonalizado/hello

##### Personalizar url no metodo:

```Java
@Path("helloPersonalizado")
public String hello() {
```
A url resultante seria:
  * http://localhost:8000/emhsexemplo/hellopersonalizado
 
##### Servir e publicar conteudo estatico:
Dessa maneira é possivel publicar paginas html ou mesmo sites completos, O endereço path tem que ser absoluto.

```Java
//Em sistema Windows
Emhs.config().setPathStatics("c:/public");

//Em sistema GNU/Linux
Emhs.config().setPathStatics("/home/username/public");
```
A url para visualizar o site publicado:
  * http://localhost:8000
 
##### Defir uma classe como serviço:

```Java
//Pode usar a anotação @Service.
@Service
public class EmhsExemplo {
```

##### Configurar diferentes formas de defir uma classe como serviço:

```Java
//Pode configurar uma ou mais Annotation.
Emhs.config().setScanAnnotation(AnnatationPersonalizada.class);
Emhs.server(8000).start();

//A definição ficaria assim
@AnnatationPersonalizada
public class EmhsExemplo {

//ou...

//Pode configurar uma ou mais SuperClasses ou Interfaces.
Emhs.config().setScanClass(SuperClasseOuInterfacePersonalizada.class);
Emhs.server(8000).start();

//A definição ficaria assim em caso de suprclase
public class EmhsExemplo extends SuperClassePersonalizada {

//A definição ficaria assim em caso de Interface
public class EmhsExemplo implements InterfacePersonalizada {
```

> também é possivel combinar as @Service com @Path para personalizar nomes tanto na declaração da classe quanto no nome do metodo.

---

Projeto exemplo: [Emhs Exemplo](https://github.com/silvafass/emhs-exemplo)

---

## Objetivo
Criar um meio de expor serviços http de forma minimalista.
## Recursos futuros
* Suporte a SSL
* Cliente java(Emhs Client)
