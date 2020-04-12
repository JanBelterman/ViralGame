package Data

object Gridlander extends Enumeration {
  type Gridlander = Value
  val DEFAULT,
      PREFER_FUNCTIONAL, PREFER_OO, PREFER_DECLARATIVE,
      SUB_PLAYER_1, SUB_PLAYER_2, SUB_PLAYER_3, SUB_PLAYER_4 = Value
}
