import "./styles.css";
import { CounterButton } from "@repo/ui/counter-button";
import { Link } from "@repo/ui/link";
import { useStore } from "@repo/zustand";

function App() {
  const { count, increment, decrement } = useStore();

  return (
    <div className="container">
      <h1 className="title">
        Admin <br />
        <span>Kitchen Sink</span>
      </h1>
      <CounterButton />
      <div className="store-counter">
        <h2>Zustand Store Counter: {count}</h2>
        <div className="buttons">
          <button onClick={increment}>Increment</button>
          <button onClick={decrement}>Decrement</button>
        </div>
      </div>
      <p className="description">
        Built With{" "}
        <Link href="https://turborepo.com" newTab>
          Turborepo
        </Link>
        {" & "}
        <Link href="https://vitejs.dev/" newTab>
          Vite
        </Link>
      </p>
    </div>
  );
}

export default App;
