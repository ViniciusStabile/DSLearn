package DSLearn.DTO;

import DSLearn.entities.Content;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContentDTO extends LessonDTO {

	private static final long serialVersionUID = 1L;

	@Size(min = 2, max = 255, message = "Minimum 2 characters and maximum 255 characters")
    @NotBlank(message = "Text content is required")
	private String textContent;
	
	@Size(min = 5, max = 255, message = "Minimum 5 characters and maximum 255 characters")
    @NotBlank(message = "Video URI is required")
	private String videoUri;

	public ContentDTO() {
		super();
	}

	public ContentDTO(Long id, String title, Integer position, String textContent, String videoUri) {
		super(id, title, position);
		this.textContent = textContent;
		this.videoUri = videoUri;
	}

	public ContentDTO(Content entity) {
		super(entity.getId(), entity.getTitle(), entity.getPosition());
		textContent = entity.getTextContent();
		videoUri = entity.getVideoUrl();
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getVideoUri() {
		return videoUri;
	}

	public void setVideoUri(String videoUri) {
		this.videoUri = videoUri;
	}

}