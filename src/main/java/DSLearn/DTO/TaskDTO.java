package DSLearn.DTO;

import DSLearn.entities.Task;

public class TaskDTO extends LessonDTO {

	private static final long serialVersionUID = 1L;

	private String description;
	private Integer questCount;
	private Integer approvalCount;
	private Double weight;

	public TaskDTO() {
		super();
	}

	public TaskDTO(Long id, String title, Integer position, String description, Integer questCount,
			Integer approvalCount, Double weight) {
		super(id, title, position);
		this.description = description;
		this.questCount = questCount;
		this.approvalCount = approvalCount;
		this.weight = weight;
	}

	public TaskDTO(Task entity) {
        super(entity.getId(), entity.getTitle(), entity.getPosition());
        description = entity.getDescription();
        questCount = entity.getQuestCount();
        approvalCount = entity.getApprovalcount();
        weight = entity.getWeight();
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

	public Integer getApprovalCount() {
		return approvalCount;
	}

	public void setApprovalCount(Integer approvalCount) {
		this.approvalCount = approvalCount;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	
}