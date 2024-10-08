package DSLearn.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_task")
public class Task extends Lesson {

	
	private static final long serialVersionUID = 1L;
	
	private String description;
	private Integer questCount;
	private Integer approvalCount;
	private Double weight;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant dueDate;
	
	public Task() {
		
	}

	public Task(Long id, String title, Integer position, Section section, String description, Integer questCount,
			Integer approvalCount, Double weight, Instant dueDate) {
		super(id, title, position, section);
		this.description = description;
		this.questCount = questCount;
		this.approvalCount = approvalCount;
		this.weight = weight;
		this.dueDate = dueDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuestCount() {
		return questCount;
	}

	public void setQuestCount(Integer questCount) {
		this.questCount = questCount;
	}

	public Integer getApprovalcount() {
		return approvalCount;
	}

	public void setApprovalcount(Integer approvalCount) {
		this.approvalCount = approvalCount;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Instant getDueDate() {
		return dueDate;
	}

	public void setDueDate(Instant dueDate) {
		this.dueDate = dueDate;
	}

	
	
}
