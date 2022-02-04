package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.OrganizationDTO;
import com.alkemy.ong.entity.Organization;
import com.alkemy.ong.entity.contact.Contact;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {

  public OrganizationDTO toDTO(Organization organization) {

    OrganizationDTO dto = new OrganizationDTO();

    dto.setName(organization.getName());
    dto.setImage(organization.getImage());
    dto.setAddress(organization.getAddress());
    dto.setPhone(organization.getPhone());
    dto.setEmail(organization.getEmail());
    dto.setWelcomeText(organization.getWelcomeText());
    dto.setAboutUsText(organization.getAboutUsText());

    Contact contact = organization.getContact();
    dto.setFacebookUrl(contact.getFacebookUrl());
    dto.setLinkedinUrl(contact.getLinkedinUrl());
    dto.setInstagramUrl(contact.getInstagramUrl());
    dto.setTimestamps(organization.getTimestamps());

    return dto;
  }

  public Organization toEntity(OrganizationDTO dto) {
    Organization org = new Organization();
    org.setName(dto.getName());
    org.setImage(dto.getImage());
    org.setAddress(dto.getAddress());
    org.setPhone(dto.getPhone());
    org.setEmail(dto.getEmail());
    org.setWelcomeText(dto.getWelcomeText());
    org.setAboutUsText(dto.getAboutUsText());

    Contact contact =
        new Contact(dto.getFacebookUrl(), dto.getLinkedinUrl(), dto.getInstagramUrl());
    org.setContact(contact);

    return org;
  }


}
