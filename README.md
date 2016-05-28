# emhs-server
Emhs Server ou Expose Method Http Service Server, é um projeto que oferece um servidor para Emhs.
====================================================================================================
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
		new EmhsServer(8000).start();
	}
}
```
Fonte: [EmhsExemplo.java](https://github.com/silvafass/emhs-exemplo/blob/master/src/br/com/emhs/exemplo/EmhsExemplo.java)

Epenas com esse trexo de codigo ja é possivel ter 2 serviços disponivel:

Caso esteja executando esse exemplo em uma maquina local, pode verificar os serviços no seguinte link pelo navegador:

  * http://localhost:8000/emhsexemplo/hello
  * http://localhost:8000/emhsexemplo/hello?nome=francisco&idade=28

====================================================================================================
Projeto exemplo: [Emhs Exemplo](https://github.com/silvafass/emhs-exemplo)
