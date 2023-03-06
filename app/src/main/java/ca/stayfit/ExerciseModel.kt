package ca.stayfit

class ExerciseModel(
    private var id: Int,
    private var name: String,
    private var image: Int,
    private var isCompleted: Boolean = false,
    private var isSelected: Boolean = false) {

    fun getId(): Int {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getImage(): Int {
        return image
    }

    fun setIsCompleted(isCompleted: Boolean) {
        this.isCompleted = isCompleted
    }

    fun getIsCompleted() : Boolean {
        return isCompleted
    }

    fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }

    fun getIsSelected() : Boolean {
        return isSelected
    }
}