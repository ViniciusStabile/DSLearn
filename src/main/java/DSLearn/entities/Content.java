package DSLearn.entities;

public class Content extends Lesson {

	
	private static final long serialVersionUID = 1L;

	private String textContent;
	private String videoUrl;
	
	public Content() {
		
	}

	public Content(Long id, String title, String position, Section section, String textContent, String videoUrl) {
		super(id, title, position, section);
		this.textContent = textContent;
		this.videoUrl = videoUrl;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	
	
}
