package com.devsuperior.bds02.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.ControllerNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;
		
	@Transactional(readOnly = true)
	public Page<EventDTO> findAllPaged(Pageable pageable) {
		Page<Event> list = eventRepository.findAll(pageable);
		return list.map(x -> new EventDTO(x));
	}

	@Transactional(readOnly = true)
	public EventDTO findById(Long id) {
		Optional<Event> obj = eventRepository.findById(id);
		Event entity = obj.orElseThrow(() -> new ControllerNotFoundException("Entity not found"));
		return new EventDTO(entity);
	}
	
	@Transactional
	public EventDTO insert(EventDTO dto) {
		Event entity = new Event();
		copyDtoToEntity(dto, entity);
		entity = eventRepository.save(entity);
		return new EventDTO(entity);
	}
	
	@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event entity = eventRepository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = eventRepository.save(entity);
			return new EventDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ControllerNotFoundException("Id not found " + id);
		}		
	}
	
	private void copyDtoToEntity(EventDTO dto, Event entity) {
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		entity.setCity(new City(dto.getCityId(), null));
	}	
}
