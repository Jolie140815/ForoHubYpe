package com.alura.ForoHubYpe;

import com.alura.ForoHubYpe.domain.topico.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ForoHubYpeApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JacksonTester<DatosRegistroTopico> jsonRegistroTopico;

	@Autowired
	private JacksonTester<DatosDetalleTopico> jsonDetalleTopico;

	@MockBean
	private TopicoRepository repository;

	@Test
	@DisplayName("Debería registrar un tópico correctamente")
	void registrarTopico() throws Exception {
		DatosRegistroTopico datos = new DatosRegistroTopico("Titulo", "Mensaje", "Autor", "Curso");
		Topico topico = new Topico(datos);

		when(repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())).thenReturn(false);
		when(repository.save(any(Topico.class))).thenReturn(topico);

		var response = mvc.perform(post("/topicos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRegistroTopico.write(datos).getJson()))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		verify(repository).save(any(Topico.class));
	}

	@Test
	@DisplayName("Debería devolver conflicto si ya existe un tópico")
	void registrarTopicoDuplicado() throws Exception {
		DatosRegistroTopico datos = new DatosRegistroTopico("Titulo", "Mensaje", "Autor", "Curso");

		when(repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())).thenReturn(true);

		var response = mvc.perform(post("/topicos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRegistroTopico.write(datos).getJson()))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
	}

	@Test
	@DisplayName("Debería listar tópicos correctamente")
	void listarTopicos() throws Exception {
		var response = mvc.perform(get("/topicos"))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		verify(repository).findAll((Pageable) any());
	}
}
