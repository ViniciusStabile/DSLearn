package DSLearn.DTO;

import DSLearn.entities.Content;

public class ContentDTO extends LessonDTO {

	private static final long serialVersionUID = 1L;

	private String textContent;
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