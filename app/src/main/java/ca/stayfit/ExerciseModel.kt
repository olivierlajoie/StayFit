package ca.stayfit

class ExerciseModel(
    private var id: Int,
    private var name: String,
    private var image: Int,
    private var isCompleted: Boolean = false,
    private var isSelected: Boolean = false) {

    fun getName(): String {
        return name
    }

    fun getImage(): Int {
        return image
    }
}