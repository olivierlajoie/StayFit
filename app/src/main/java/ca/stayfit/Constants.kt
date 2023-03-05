package ca.stayfit

object Constants {
    fun defaultExerciseList(): ArrayList<ExerciseModel> {
        val exerciseList = ArrayList<ExerciseModel>()

        exerciseList.add(ExerciseModel(
            1,
            "Balance Ball",
            R.drawable.balanceball
        ))

        exerciseList.add(ExerciseModel(
            2,
            "Body Combat",
            R.drawable.bodycombat
        ))

        exerciseList.add(ExerciseModel(
            3,
            "Boxing",
            R.drawable.boxing
        ))

        exerciseList.add(ExerciseModel(
            4,
            "Deadlift",
            R.drawable.deadlift
        ))

        exerciseList.add(ExerciseModel(
            5,
            "Dumbbells",
            R.drawable.dumbbells
        ))

        exerciseList.add(ExerciseModel(
            6,
            "Pushups",
            R.drawable.pushups
        ))

        exerciseList.add(ExerciseModel(
            7,
            "Pullups",
            R.drawable.pullups
        ))

        return exerciseList
    }
}