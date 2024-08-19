package DSLearn.DTO;

import DSLearn.entities.Task;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class TaskDTO extends LessonDTO {

	private static final long serialVersionUID = 1L;

	@Size(max = 255, message = "Description cannot exceed 255 characters")
	private String description;

	@PositiveOrZero(message = "Quest count must be zero or positive")
	@NotNull(message = "Quest count is required")
	private Integer questCount;

	@PositiveOrZero(message = "Approval count must be zero or positive")
	@NotNull(message = "Approval count is required")
	private Integer approvalCount;

	@DecimalMin(value = "0.0", inclusive = false, message = "Weight must be greater than 0")
	@DecimalMax(value = "100.0", message = "Weight must be less than or equal to 100")
	@NotNull(message = "Weight is required")
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