//package kevin.event;
//
//import java.lang.reflect.Method;
//import java.util.*;
//
///**
// * Manages event listeners using reflection.
// */
//public class EventManager {
//    private final HashMap<Class<? extends Event>, List<EventHook>> registry = new HashMap<>();
//
//    public void registerListener(Listenable listener) {
//        for (Method method : listener.getClass().getDeclaredMethods()) {
//            if (method.isAnnotationPresent(EventTarget.class)) {
//                if (!method.isAccessible()) {
//                    method.setAccessible(true);
//                }
//
//                Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];
//                EventTarget eventTarget = method.getAnnotation(EventTarget.class);
//
//                List<EventHook> targets = registry.getOrDefault(eventClass, new ArrayList<>());
//                targets.add(new EventHook(listener, method, eventTarget));
//                registry.put(eventClass, targets);
//            }
//        }
//    }
//
//    public void unregisterListener(Listenable listener) {
//        for (Map.Entry<Class<? extends Event>, List<EventHook>> entry : registry.entrySet()) {
//            List<EventHook> targets = entry.getValue();
//            targets.removeIf(target -> target.listener == listener);
//
//            entry.setValue(targets);
//        }
//    }
//
//    public void callEvent(Event event) {
//        List<EventHook> targets = registry.get(event.getClass());
//
//        if (targets != null) {
//            for (EventHook hook : targets) {
//                try {
//                    if (!hook.isIgnoreCondition && hook.eventClass.handleEvents()) {
//                        hook.method.invoke(hook.eventClass, event);
//                    }
//                } catch (Exception e) {
//                    if (KevinClient.debug) {
//                        synchronized ("Penis Lock") {
//                            e.printStackTrace();
//                            if (e instanceof InvocationTargetException) {
//                                KevinClient.hud.addNotification(
//                                        new Notification(
//                                                "Exception caught when calling " + event.getClass().getSimpleName() +
//                                                        " in " + hook.eventClass.getClass().getSimpleName() + ": " +
//                                                        ((InvocationTargetException) e).getTargetException().getMessage(),
//                                                "Debug",
//                                                ConnectNotificationType.Error
//                                        )
//                                );
//                                Minecraft.getLogger().warn("Exception caught when calling " + event.getSimpleName() +
//                                        " in listener " + hook.eventClass.getSimpleName() + ":\n" +
//                                        e.getStackTrace().toString());
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * A helper class to hold the event hook details.
//     */
//    private static class EventHook {
//        final Listenable listener;
//        final Method method;
//        final EventTarget eventTarget;
//
//        public EventHook(Listenable listener, Method method, EventTarget eventTarget) {
//            this.listener = listener;
//            this.method = method;
//            this.eventTarget = eventTarget;
//        }
//
//        public boolean isIgnoreCondition() {
//            return false;
//        }
//
//        public Class<? extends Event> getEventClass() {
//            return (Class<? extends Event>) method.getParameterTypes()[0];
//        }
//    }
//}
